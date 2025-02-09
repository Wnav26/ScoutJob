package vn.wnav.jobhunter.util;

import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletResponse;
import vn.wnav.jobhunter.domain.response.RestResponse;
import vn.wnav.jobhunter.util.annotation.ApiMessage;

@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    @Nullable
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType,
            Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();

        // Check if the status code represents an error
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(status);
        if (body instanceof String || body instanceof Resource) {
            return body;
        }
        if (status >= 400) {
            return body;
        } else {
            res.setData(body);
            ApiMessage mess = returnType.getMethodAnnotation(ApiMessage.class);
            res.setMessage(mess != null ? mess.value() : "CALL API SUCCESS");
        }
        return res;
    }

}
