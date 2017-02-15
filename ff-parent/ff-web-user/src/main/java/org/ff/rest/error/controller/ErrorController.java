//package org.ff.rest.error.controller;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.ff.rest.error.resource.ErrorResource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.web.ErrorAttributes;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@RestController
//public class ErrorController implements org.springframework.boot.autoconfigure.web.ErrorController {
//
//	private static final String PATH = "/error";
//
//	@Autowired
//	private ErrorAttributes errorAttributes;
//
//	@Autowired
//	private ObjectMapper objectMapper;
//
//	@RequestMapping(value = PATH)
//	public String error(HttpServletRequest request, HttpServletResponse response) {
//		ErrorResource error = getErrorAttributes(request);
//		return error.getStatus() + " " + error.getMessage();
//	}
//
//	@Override
//	public String getErrorPath() {
//		return PATH;
//	}
//
//	private ErrorResource getErrorAttributes(HttpServletRequest request) {
//		ErrorResource result = null;
//		try {
//			result = objectMapper.readValue(objectMapper.writeValueAsString(errorAttributes.getErrorAttributes(new ServletRequestAttributes(request), false)), ErrorResource.class);
//		} catch (Exception e) {
//			log.error(e.getMessage(), e);
//		}
//		return result;
//	}
//
//}