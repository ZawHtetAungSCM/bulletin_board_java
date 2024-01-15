package com.mtm.bulletin_board.tags;

import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;
import jakarta.servlet.jsp.PageContext;

public class FormError extends SimpleTagSupport {

    private String paramName;

    public void setError(String paramName) {
        this.paramName = paramName;
    }

    @Override
    public void doTag() throws JspException, IOException {
        PageContext pageContext = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        String jsonString = (String) request.getAttribute("error");
        JsonObject jsonObject = !(jsonString == null || jsonString.isEmpty())
                ? JsonParser.parseString(jsonString).getAsJsonObject()
                : null;

        if (jsonObject != null && !jsonObject.isJsonNull()) {

            JsonElement element = jsonObject.get(paramName);
            if (element != null && !element.isJsonNull()) {
                String errorMessage = element.getAsString();
                getJspContext().getOut().write("<div class=\"form-error\">" + errorMessage + "</div>");
            }
        }
    }
}
