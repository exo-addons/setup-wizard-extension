<%--

    Copyright (C) 2009 eXo Platform SAS.
    
    This is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation; either version 2.1 of
    the License, or (at your option) any later version.
    
    This software is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
    Lesser General Public License for more details.
    
    You should have received a copy of the GNU Lesser General Public
    License along with this software; if not, write to the Free
    Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
    02110-1301 USA, or see the FSF site: http://www.fsf.org.

--%>
<%@ page import="org.exoplatform.container.PortalContainer"%>
<%@ page language="java" %>
<%
  String contextPath = request.getContextPath();
  String lang = request.getLocale().getLanguage();
  
  response.setCharacterEncoding("UTF-8"); 
  response.setContentType("text/html; charset=UTF-8");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="<%=lang%>" lang="<%=lang%>" dir="ltr">
  <head>
    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <title>Welcome to eXo Platform</title>
    
    <link href="<%=contextPath%>/bootstrap/css/bootstrap.css" rel="stylesheet" type="text/css" />
    
    <link href="<%=contextPath%>/css/Stylesheet.css" rel="stylesheet" type="text/css" />
		<link rel="shortcut icon" type="image/png" href="<%=contextPath%>/favicon.png" />
  </head>
  
  <body onLoad="SetupWizard.initSetupWizard();">
  
  <div class="container">
  
    <!-- Modal -->
    <div id="myModal" class="modal hide fade in" style="display: none; ">
      <div class="modal-header">
        <a class="close" data-dismiss="modal">×</a>
        <h3>Message</h3>
      </div>
      <div class="modal-body">
        <p>Message ...</p>		        
      </div>
      <div class="modal-footer">
        <a href="#" class="btn" data-dismiss="modal">Close</a>
      </div>
    </div>
    
    <div id="global_loader" class="SetupWizardPopup" style="display: none">
      <div style="text-align:center"><img src="./background/loader.gif" /></div>
    </div>
    
    <div id="setup1" class="SetupWizardPopup" style="display: none">
      <%@ include file="screen/Setup.jsp"%>
    </div>

    <div id="setup3" class="SetupWizardPopup" style="display: none">
      <%@ include file="screen/SuperUser.jsp"%>
    </div>

    <div id="setup4" class="SetupWizardPopup" style="display: none">
      <%@ include file="screen/JcrDb.jsp"%>
    </div>

    <div id="setup5" class="SetupWizardPopup" style="display: none">
      <%@ include file="screen/IdmSetup.jsp"%>
    </div>

    <div id="setup6" class="SetupWizardPopup" style="display: none">
      <%@ include file="screen/IdmDb.jsp"%>
    </div>

    <div id="setup7" class="SetupWizardPopup" style="display: none">
      <%@ include file="screen/MailSetting.jsp"%>
    </div>

    <div id="setup8" class="SetupWizardPopup" style="display: none">
      <%@ include file="screen/Website.jsp"%>
    </div>
    
    <div id="setup9" class="SetupWizardPopup" style="display: none">
      <%@ include file="screen/Summary.jsp"%>
    </div>
    
    <div id="setup10" class="SetupWizardPopup" style="display: none">
      <%@ include file="screen/Finish.jsp"%>
    </div>
    
    <form style="display: none;" name="SetupExitForm" id="SetupExitForm" method="POST" action="#" >
    </form>
  
  </div>
  
    
  <script type="text/javascript" src="<%=contextPath%>/js/jquery-latest.js"></script>
  <script type="text/javascript" src="<%=contextPath%>/bootstrap/js/bootstrap.js"></script>
  <script type="text/javascript" src="<%=contextPath%>/js/setupwizard/setupwizard.js"></script>
  </body>
</html>