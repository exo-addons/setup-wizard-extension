/**
 * Module used to manage Setup wizard JS features
 *
 */
var SetupWizard = {};

SetupWizard.NB_SCREENS = 10;
SetupWizard.SETUP_TYPE = "standard";
SetupWizard.SETUP_DB_TYPE = "standard";
SetupWizard.INPUT_MIN_SIZE = 3;
SetupWizard.INPUT_MAX_SIZE = 20;
SetupWizard.EMAIL_REGEXP = /^([a-zA-Z0-9_\.\-])+\@((([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+)$/;
SetupWizard.DEBUG_MODE = false;
SetupWizard.FIRST_SCREEN = 1;

/** PROPERTIES **/
SetupWizard.SU_USERNAME="su_username";
SetupWizard.SU_PASSWORD="su_password";
SetupWizard.SU_EMAIL="su_email";
SetupWizard.JCR_DATA_SOURCE="jcr_data_source";
SetupWizard.STORE_FILES_IN_DB="store_files_in_db";
SetupWizard.IDM_DATA_SOURCE="idm_data_source";
SetupWizard.LDAP_SERVER_TYPE="ldap_server_type";
SetupWizard.LDAP_PROVIDER_URL="ldap_provider_url";
SetupWizard.LDAP_BASE_DN="ldap_base_dn";
SetupWizard.LDAP_ROOT_DN="ldap_root_dn";
SetupWizard.LDAP_PASSWORD="ldap_password";
SetupWizard.FS_LOGS="fs_logs";
SetupWizard.FS_INDEX="fs_index";
SetupWizard.FS_DATA_VALUES="fs_data_values";
SetupWizard.SMTP_HOST="smtp_host";
SetupWizard.SMTP_PORT="smtp_port";
SetupWizard.SMTP_SECURED_CONNECTION="smtp_secured_connection";
SetupWizard.SMTP_USERNAME="smtp_username";
SetupWizard.SMTP_PASSWORD="smtp_password";
SetupWizard.SMTP_EMAIL="smtp_email";
SetupWizard.WS_BLANK="ws_blank";
SetupWizard.WS_SAMPLES="ws_samples";


/*===========================================================================================================*
 *       GLOBAL METHODS
 *===========================================================================================================*/

SetupWizard.initSetupWizard = function() {
  if (!window.console) console = {};
  console.log = console.log || function(){};
  console.warn = console.warn || function(){};
  console.error = console.error || function(){};
  console.info = console.info || function(){};
  
  if(SetupWizard.FIRST_SCREEN != -1) {
    SetupWizard.showStep(SetupWizard.FIRST_SCREEN);
  }
  
  // TODO Pre fill all screens
}

SetupWizard.showStep = function(n) {
  for(var i=1; i<=SetupWizard.NB_SCREENS; i++) {
    $('#setup' + i).hide();
  }
  $('#setup' + n).show();
}

SetupWizard.showMessage = function(title, message) {
  if(title != undefined && title.length > 0 && message != undefined && message.length > 0) {
    $('.modal-header > h3').html(title);
    $('.modal-body').html(message);
    // display modal
    $('#myModal').modal();
  }
}

SetupWizard.showErrors = function(errors) {
  if(errors != undefined && errors.length > 0) {
    // Add errors to the modal
    var errHtml = "<ul>";
    for(var i=0; i<errors.length; i++) {
      errHtml += "<li>" + errors[i] + "</li>";
    }
    errHtml += "</ul>";
    
    SetupWizard.showMessage("Error", errHtml);
  }
}

SetupWizard.showError = function(error) {
  if(error != undefined && error.length > 0) {
    SetupWizard.showMessage("Error", error);
  }
}


/*===========================================================================================================*
 *       VALIDATION METHODS
 *===========================================================================================================*/

/**
 * Setup
 */
SetupWizard.validateStep1 = function(event) {
  SetupWizard.showStep(2);
}

/**
 * SetupType
 */
SetupWizard.validateStep2 = function() {
  // store wizard type
  SetupWizard.SETUP_TYPE = $('input[name=setupTypeOptions]:checked').val();
  
  SetupWizard.showStep(3);
}

/**
 * SuperUser
 */
SetupWizard.validateStep3 = function() {

  if(SetupWizard.DEBUG_MODE) {
    SetupWizard.showStep(4);
    return;
  }
  
  var username = $("#inputUsername").val();
  var password = $("#inputPassword").val();
  var confirmPassword = $("#inputConfirmPassword").val();
  var email = $("#inputEmail").val();
  var errors = new Array();
  
  if(!(username != undefined && username.length > SetupWizard.INPUT_MIN_SIZE && username.length < SetupWizard.INPUT_MAX_SIZE)) {
    errors.push("Verify field UserName");
  }
  if(!(password != undefined && password.length > SetupWizard.INPUT_MIN_SIZE && password.length < SetupWizard.INPUT_MAX_SIZE)) {
    errors.push("Verify field Password");
  }
  if(!(confirmPassword != undefined && confirmPassword == password)) {
    errors.push("Passwords need to be identical");
  }
  if(!(email != undefined && SetupWizard.EMAIL_REGEXP.test(email))) {
    errors.push("Verify field email");
  }

  if(errors.length > 0) {
    SetupWizard.showErrors(errors);
  }
  else {
    SetupWizard.writePropertyInDom(SetupWizard.SU_USERNAME, username);
    SetupWizard.writePropertyInDom(SetupWizard.SU_PASSWORD, password);
    SetupWizard.writePropertyInDom(SetupWizard.SU_EMAIL, email);
    SetupWizard.showStep(4);
  }
}

/**
 * JcrDb
 */
SetupWizard.validateStep4 = function() {

  var ownDsIsChecked = ($("input[id='radioJcrOwnDs']:checked").length > 0);
  var inputDs = $("#inputJcrDs").val();
  var selectDs = $("#selectJcrDs").val();
  var checkboxStore = ($("input[id='checkboxStore']:checked").length > 0);
  var ds;
  var errors = new Array();
  
  if(ownDsIsChecked) {
    ds = inputDs;
    if(!(ds != undefined && ds.length > SetupWizard.INPUT_MIN_SIZE && ds.length < SetupWizard.INPUT_MAX_SIZE)) {
      errors.push("Verify field datasource");
    }
  }
  else {
    ds = selectDs;
  }

  if(errors.length > 0) {
    SetupWizard.showErrors(errors);
  }
  else {
    SetupWizard.writePropertyInDom(SetupWizard.JCR_DATA_SOURCE, ds);
    SetupWizard.writePropertyInDom(SetupWizard.STORE_FILES_IN_DB, checkboxStore);
    SetupWizard.showStep(5);
  }
}

/**
 * IdmSetup
 */
SetupWizard.validateStep5 = function() {

  // store db type
  SetupWizard.SETUP_TYPE = $('input[name=radioIdmSetup]:checked').val();
  
  SetupWizard.showStep(6);
}

/**
 * IdmDb
 */
SetupWizard.validateStep6 = function() {

  var ownDsIsChecked = ($("input[id='radioIdmDs2']:checked").length > 0);
  var inputDs = $("#inputIdmDs").val();
  var selectDs = $("#selectIdmDs").val();
  var ds;
  var errors = new Array();
  
  if(ownDsIsChecked) {
    ds = inputDs;
    if(!(ds != undefined && ds.length > SetupWizard.INPUT_MIN_SIZE && ds.length < SetupWizard.INPUT_MAX_SIZE)) {
      errors.push("Verify field datasource");
    }
  }
  else {
    ds = selectDs;
  }

  if(errors.length > 0) {
    SetupWizard.showErrors(errors);
  }
  else {
    SetupWizard.writePropertyInDom(SetupWizard.IDM_DATA_SOURCE, ds);
    SetupWizard.showStep(7);
  }
}

/**
 * MailSetting
 */
SetupWizard.validateStep7 = function() {
  
  var inputSmtpHost  = $("#inputMailSmtpHost").val();
  var inputPort      = $("#inputMailPort").val();
  var checkboxSecure = ($("input[id='checkboxMailSecure']:checked").length > 0);
  var inputUserName  = $("#inputMailUserName").val();
  var inputPassword  = $("#inputMailPassword").val();
  var inputEmail     = $("#inputMailEmail").val();
  
  var errors = new Array();
  
  if(!(inputSmtpHost != undefined && inputSmtpHost.length > SetupWizard.INPUT_MIN_SIZE && inputSmtpHost.length < SetupWizard.INPUT_MAX_SIZE)) {
    errors.push("Verify field SmtpHost");
  }
  if(!(inputPort != undefined && inputPort.length > 0 && inputPort.length < SetupWizard.INPUT_MAX_SIZE && $.isNumeric(inputPort))) {
    errors.push("Verify field Port");
  }
  if(!(inputUserName != undefined && inputUserName.length > SetupWizard.INPUT_MIN_SIZE && inputUserName.length < SetupWizard.INPUT_MAX_SIZE)) {
    errors.push("Verify field UserName");
  }
  if(!(inputPassword != undefined && inputPassword.length > SetupWizard.INPUT_MIN_SIZE && inputPassword.length < SetupWizard.INPUT_MAX_SIZE)) {
    errors.push("Verify field Password");
  }
  if(!(inputEmail != undefined && SetupWizard.EMAIL_REGEXP.test(inputEmail))) {
    errors.push("Verify field Email");
  }

  if(errors.length > 0) {
    SetupWizard.showErrors(errors);
  }
  else {
    SetupWizard.writePropertyInDom(SetupWizard.SMTP_HOST, inputSmtpHost);
    SetupWizard.writePropertyInDom(SetupWizard.SMTP_PORT, inputPort);
    SetupWizard.writePropertyInDom(SetupWizard.SMTP_SECURED_CONNECTION, checkboxSecure);
    SetupWizard.writePropertyInDom(SetupWizard.SMTP_USERNAME, inputUserName);
    SetupWizard.writePropertyInDom(SetupWizard.SMTP_PASSWORD, inputPassword);
    SetupWizard.writePropertyInDom(SetupWizard.SMTP_EMAIL, inputEmail);
    SetupWizard.showStep(8);
  }
}

/**
 * Website
 */
SetupWizard.validateStep8 = function() {
  
  var radioWebsiteBlank = ($("input[id='radioWebsiteBlank']:checked").length > 0);
  var radioWebsiteInstall = ($("input[id='radioWebsiteInstall']:checked").length > 0);
  var radioWebsites = $("input[name='optionsWebSite']:checked").serializeArray();
  var websites = "";
  
  if(radioWebsiteInstall) {
    for(var i=0; i<radioWebsites.length; i++) {
      websites += radioWebsites[i].value;
      if(i<radioWebsites.length-1) {websites += ",";}
    }
  }

  SetupWizard.writePropertyInDom(SetupWizard.WS_BLANK, radioWebsiteBlank);
  SetupWizard.writePropertyInDom(SetupWizard.WS_SAMPLES, websites);
  
  SetupWizard.showStep(9);
}

/**
 * Summary
 */
SetupWizard.validateStep9 = function() {

  // Validate Form with WS writeProperties, if response is ok, call WS startPlatform and display next screen
  $.post(
    "/setup/setuprest/service/wp", 
    $("#SetupExitForm").serialize(),
    function(data) {}, 
    "json"
  )
  .complete(function(data) {
    if(data.responseText == "ok") {
      $.ajax({
        url: "/setup/setuprest/service/sp"
      })
      .always(function (data) {
        if(data.responseText == "ok") {
          SetupWizard.showStep(10);
        }
        else {
          SetupWizard.showError("Impossible to start platform, verify your data and try again.");
        }
      });
    }
    else {
      SetupWizard.showError("Impossible to write properties, verify your data and try again.");
    }
  }); 
  
  
}

/**
 * Finish
 */
SetupWizard.validateStep10 = function() {
  SetupWizard.exit();
}

SetupWizard.exit = function() {
  $("#SetupExitForm").submit();
}



/*===========================================================================================================*
 *       UTILITY METHODS
 *===========================================================================================================*/

/**
 * This method permit to write a property (name and value) into the dom, in a form as an hidden input
 * If the property is yet written, it's just an update
 */
SetupWizard.writePropertyInDom = function(ppName, ppValue) {
  // Test if property is yet written
  if ($("#" + ppName).length > 0) {
    // update
    $("#" + ppName).val(ppValue);
  }
  else {
    // create element
    $('<input>').attr({
        type: 'hidden',
        id: ppName,
        name: ppName,
        value: ppValue
    }).appendTo('#SetupExitForm');
  }
  return;
}