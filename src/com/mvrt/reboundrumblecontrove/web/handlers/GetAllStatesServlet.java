package com.mvrt.reboundrumblecontrove.web.handlers;

import com.m3rcuriel.controve.retrievable.SystemManager;

import org.json.simple.JSONObject;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetAllStatesServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("application/json;charset=utf-8");
    response.setStatus(HttpServletResponse.SC_OK);
    JSONObject json = SystemManager.getInstance().get();
    response.getWriter().println(json.toJSONString());
  }
}
