package com.sdtvnews.sdtvnews.config;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        System.out.println(status);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            System.out.println(statusCode);
            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "dashboard/error-403"; // Redirect to 403 error page
            } else if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "dashboard/error-404"; // Redirect to 403 error page
            }else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()){
                return "dashboard/error-500";
            }
        }
        return "dashboard/error-500"; // Default error page
    }

}
