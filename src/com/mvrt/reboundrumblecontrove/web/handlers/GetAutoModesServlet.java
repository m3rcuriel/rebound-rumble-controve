package com.mvrt.reboundrumblecontrove.web.handlers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class GetAutoModesServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("application/json;charset=utf-8");
    response.setStatus(HttpServletResponse.SC_OK);
    response.setHeader("Access-Control-Allow-Origin", "*");
    // JSONArray allAuto = AutoModeSelector.getInstance().getAutoModeJSONList();
    // response.getWriter().println(allAuto.toJSONString());
  }

}
