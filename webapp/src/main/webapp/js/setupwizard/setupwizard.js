/**
 * Module used to manage Setup wizard JS features
 *
 */
var SetupWizard = {};

/** GLOBAL VARIABLES **/
SetupWizard.NB_SCREENS = 10;
SetupWizard.SETUP_DB_TYPE = "standard";
SetupWizard.INPUT_MIN_SIZE = 3;
SetupWizard.INPUT_MAX_SIZE = 40;
SetupWizard.EMAIL_REGEXP = /^([a-zA-Z0-9_\.\-])+\@((([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+)$/;
SetupWizard.INIT_NB_AJAX = 0;
SetupWizard.INIT_TOTAL_AJAX = 3;

/**
 * SERVER VARIABLES
 * If his value is undefined, variable is initialized with server one
 */
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

/** LABELS OF PROPERTIES **/
SetupWizard.LABELS_PP = {};
SetupWizard.LABELS_PP[SetupWizard.SU_USERNAME] = "Super User name";
SetupWizard.LABELS_PP[SetupWizard.SU_PASSWORD] = "Super User Password";
SetupWizard.LABELS_PP[SetupWizard.SU_EMAIL] = "Super User email";
SetupWizard.LABELS_PP[SetupWizard.JCR_DATA_SOURCE] = "JCR data source";
SetupWizard.LABELS_PP[SetupWizard.STORE_FILES_IN_DB] = "File stored in DB";
SetupWizard.LABELS_PP[SetupWizard.IDM_DATA_SOURCE] = "IDM data source";
SetupWizard.LABELS_PP[SetupWizard.LDAP_SERVER_TYPE] = "LDAP server type";
SetupWizard.LABELS_PP[SetupWizard.LDAP_PROVIDER_URL] = "LDAP provider url";
SetupWizard.LABELS_PP[SetupWizard.LDAP_BASE_DN] = "LDAP base DN";
SetupWizard.LABELS_PP[SetupWizard.LDAP_ROOT_DN] = "LDAP toor DN";
SetupWizard.LABELS_PP[SetupWizard.LDAP_PASSWORD] = "LDAP password";
SetupWizard.LABELS_PP[SetupWizard.FS_LOGS] = "Logs";
SetupWizard.LABELS_PP[SetupWizard.FS_INDEX] = "Index";
SetupWizard.LABELS_PP[SetupWizard.FS_DATA_VALUES] = "Data values";
SetupWizard.LABELS_PP[SetupWizard.SMTP_HOST] = "Smtp host";
SetupWizard.LABELS_PP[SetupWizard.SMTP_PORT] = "Smtp port";
SetupWizard.LABELS_PP[SetupWizard.SMTP_SECURED_CONNECTION] = "Smtp secured connection";
SetupWizard.LABELS_PP[SetupWizard.SMTP_USERNAME] = "Smtp username";
SetupWizard.LABELS_PP[SetupWizard.SMTP_PASSWORD] = "Smtp password";
SetupWizard.LABELS_PP[SetupWizard.SMTP_EMAIL] = "Smtp email";
SetupWizard.LABELS_PP[SetupWizard.WS_BLANK] = "Blank web site";
SetupWizard.LABELS_PP[SetupWizard.WS_SAMPLES] = "Installed web sites";

/** DEFAULT VALUES OF PROPERTIES **/
SetupWizard.DEFAULT_VALUES_PP = {};


/*===========================================================================================================*
 *       GLOBAL METHODS
 *===========================================================================================================*/

/**
 * First method called before displaying Setup Wizard
 */
SetupWizard.initSetupWizard = function() {
  if (!window.console) console = {};
  console.log = console.log || function(){};
  console.warn = console.warn || function(){};
  console.error = console.error || function(){};
  console.info = console.info || function(){};
  
  SetupWizard.displayGlobalLoader();
  SetupWizard.initSystemProperties();
  SetupWizard.initDataSources();
  SetupWizard.initStartupInformations();
}

SetupWizard.initSystemProperties = function() {
  $.ajax({
    url: "/setup/setuprest/service/pp",
    success: function(data) {
      var javaMap = data.systemPropertiesDto.data.entry;
      $.each(javaMap, function(i, item){
        vi =  item.value;
        if (vi != undefined){
          var key = item.key.$;
          var value = vi.$;
          SetupWizard.writeNewRow("SystemPropertiesTable", key, value);
        }
      });

      SetupWizard.INIT_NB_AJAX++;
      SetupWizard.finalizeInitSetupWizard();
    }
  });
}

SetupWizard.initDataSources = function() {
  $.ajax({
    url: "/setup/setuprest/service/ds",
    success: function(data) {
      var javaList = data.datasourcesDto.data;
      $.each(javaList, function(i, item){
        var elemList = item.$;
        $("#selectJcrDs").append("<option>"+elemList+"</option>");
        $("#selectIdmDs").append("<option>"+elemList+"</option>");
      });
      
      SetupWizard.INIT_NB_AJAX++;
      SetupWizard.finalizeInitSetupWizard();
    }
  });
}

SetupWizard.initStartupInformations = function() {
  // Ajax request to get all startup informations
  $.ajax({
    url: "/setup/setuprest/service/si"
  })
  .always(function (data) {
    // Init 2 global variables FIRST_SCREEN & DEBUG_MODE
    if(data != undefined && data.StartupInformationDto != undefined) {
      if(SetupWizard.FIRST_SCREEN == undefined && data.StartupInformationDto.firstScreenNumber.$ != undefined) {
        SetupWizard.FIRST_SCREEN = data.StartupInformationDto.firstScreenNumber.$;
      }
      if(SetupWizard.DEBUG_MODE == undefined && data.StartupInformationDto.isDebugActivated.$ != undefined) {
        SetupWizard.DEBUG_MODE = data.StartupInformationDto.isDebugActivated.$;
      }
    
      // Fetch all properties and fill a table
      var propertiesValues = data.StartupInformationDto.propertiesValues.entry;
      if(propertiesValues != undefined) {
        for(var i=0; i<propertiesValues.length; i++) {
          SetupWizard.DEFAULT_VALUES_PP[propertiesValues[i].key.$] = propertiesValues[i].value.$;
        }
      }
    }
    
    // With pp recovered, pre fill all fields of setup wizard
    SetupWizard.preFillFields();
    
    SetupWizard.INIT_NB_AJAX++;
    SetupWizard.finalizeInitSetupWizard();
  });
}

/**
 * Callback method called at the return of ajax request from init method
 */
SetupWizard.finalizeInitSetupWizard = function() {
  
  if(SetupWizard.INIT_NB_AJAX >= SetupWizard.INIT_TOTAL_AJAX) {
    // hide global loader
    SetupWizard.hideGlobalLoader();
    
    // Display first screen
    if(SetupWizard.FIRST_SCREEN != -1) {
      SetupWizard.showStep(SetupWizard.FIRST_SCREEN);
    }
  }
}

/**
 * pre fill all steps which need an initialization
 */
SetupWizard.preFillFields = function() {

  // Step3: SuperUser
  if(SetupWizard.DEFAULT_VALUES_PP[SetupWizard.SU_USERNAME] != undefined && $("#inputUsername").val() == "") {
    $("#inputUsername").val(SetupWizard.DEFAULT_VALUES_PP[SetupWizard.SU_USERNAME]);
  }
  if(SetupWizard.DEFAULT_VALUES_PP[SetupWizard.SU_PASSWORD] != undefined && $("#inputPassword").val() == "") {
    $("#inputPassword").val(SetupWizard.DEFAULT_VALUES_PP[SetupWizard.SU_PASSWORD]);
  }
  if(SetupWizard.DEFAULT_VALUES_PP[SetupWizard.SU_PASSWORD] != undefined && $("#inputConfirmPassword").val() == "") {
    $("#inputConfirmPassword").val(SetupWizard.DEFAULT_VALUES_PP[SetupWizard.SU_PASSWORD]);
  }
  if(SetupWizard.DEFAULT_VALUES_PP[SetupWizard.SU_EMAIL] != undefined && $("#inputEmail").val() == "") {
    $("#inputEmail").val(SetupWizard.DEFAULT_VALUES_PP[SetupWizard.SU_EMAIL]);
  }
  
  // Step4: JcrDb
  if(SetupWizard.DEFAULT_VALUES_PP[SetupWizard.STORE_FILES_IN_DB] != undefined) {
    $("#checkboxStore").attr('checked', SetupWizard.DEFAULT_VALUES_PP[SetupWizard.STORE_FILES_IN_DB]);
  }
  
  // pre fill datasource JCR fields
  var jcrDs = SetupWizard.DEFAULT_VALUES_PP[SetupWizard.JCR_DATA_SOURCE];
  if(jcrDs != undefined) {
    var dss = $.map($("#selectJcrDs").children(),function(a){return a.value;});
    if($.inArray(jcrDs, dss) < 0) {
      $("input[id='radioJcrOwnDs']").attr('checked', true);
      $("#inputJcrDs").val(SetupWizard.DEFAULT_VALUES_PP[SetupWizard.JCR_DATA_SOURCE]);
    }
  }
  
  // Step6: IdmDb
  var idmDs = SetupWizard.DEFAULT_VALUES_PP[SetupWizard.IDM_DATA_SOURCE];
  if(idmDs != undefined) {
    var dssIdm = $.map($("#selectIdmDs").children(),function(a){return a.value;});
    if($.inArray(idmDs, dssIdm) < 0) {
      $("input[id='radioIdmDs2']").attr('checked', true);
      $("#inputIdmDs").val(SetupWizard.DEFAULT_VALUES_PP[SetupWizard.IDM_DATA_SOURCE]);
    }
  }
  
  // Step7: MailSetting
  if(SetupWizard.DEFAULT_VALUES_PP[SetupWizard.SMTP_HOST] != undefined && $("#inputMailSmtpHost").val() == "") {
    $("#inputMailSmtpHost").val(SetupWizard.DEFAULT_VALUES_PP[SetupWizard.SMTP_HOST]);
  }
  if(SetupWizard.DEFAULT_VALUES_PP[SetupWizard.SMTP_PORT] != undefined && $("#inputMailPort").val() == "") {
    $("#inputMailPort").val(SetupWizard.DEFAULT_VALUES_PP[SetupWizard.SMTP_PORT]);
  }
  if(SetupWizard.DEFAULT_VALUES_PP[SetupWizard.SMTP_SECURED_CONNECTION] != undefined) {
    $("#checkboxMailSecure").attr('checked', SetupWizard.getBoolean(SetupWizard.DEFAULT_VALUES_PP[SetupWizard.SMTP_SECURED_CONNECTION]));
  }
  if(SetupWizard.DEFAULT_VALUES_PP[SetupWizard.SMTP_USERNAME] != undefined && $("#inputMailUserName").val() == "") {
    $("#inputMailUserName").val(SetupWizard.DEFAULT_VALUES_PP[SetupWizard.SMTP_USERNAME]);
  }
  if(SetupWizard.DEFAULT_VALUES_PP[SetupWizard.SMTP_PASSWORD] != undefined && $("#inputMailPassword").val() == "") {
    $("#inputMailPassword").val(SetupWizard.DEFAULT_VALUES_PP[SetupWizard.SMTP_PASSWORD]);
  }
  if(SetupWizard.DEFAULT_VALUES_PP[SetupWizard.SMTP_EMAIL] != undefined && $("#inputMailEmail").val() == "") {
    $("#inputMailEmail").val(SetupWizard.DEFAULT_VALUES_PP[SetupWizard.SMTP_EMAIL]);
  }
  
  // Step8: Website
  $("#radioWebsiteInstall").attr("checked", true);
  if(SetupWizard.DEFAULT_VALUES_PP[SetupWizard.WS_BLANK] != undefined) {
    var bbBlank = SetupWizard.getBoolean(SetupWizard.DEFAULT_VALUES_PP[SetupWizard.WS_BLANK]);
    if(bbBlank) {
      $("#radioWebsiteBlank").attr("checked", true);
    }
    else {
      $("#radioWebsiteInstall").attr("checked", true);
    }
  }
  var samples = SetupWizard.DEFAULT_VALUES_PP[SetupWizard.WS_SAMPLES];
  if(samples != undefined) {
    // HACK: we need to transform pp "[toto1, toto2]" to "toto1,toto2" to have a good array
    // it's a problem from WS which returns this kind of string
    var arrSamples = samples.replace(/\[|\s+|\]/g,"").split(",");
    for(var i=0; i<arrSamples.length; i++) {
      $("input[value=" + arrSamples[i] + "]").attr("checked", true);
    }
  }
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

SetupWizard.displayLoader = function(noScreen) {
  $('#setup' + noScreen + ' .loader_ctr').show();
  SetupWizard.lockScreen(noScreen);
}

SetupWizard.hideLoader = function(noScreen) {
  $('#setup' + noScreen + ' .loader_ctr').hide();
  SetupWizard.unlockScreen(noScreen);
}

SetupWizard.lockScreen = function(noScreen) {
  $("#setup" + noScreen + " input").attr("disabled", true);
}

SetupWizard.unlockScreen = function(noScreen) {
  $("#setup" + noScreen + " input").attr("disabled", false);
}

SetupWizard.displayGlobalLoader = function(noScreen) {
  $('#global_loader').show();
}

SetupWizard.hideGlobalLoader = function(noScreen) {
  $('#global_loader').hide();
}



/*===========================================================================================================*
 *       VALIDATION METHODS
 *===========================================================================================================*/

/**
 * Setup
 */
SetupWizard.validateStep1 = function(event) {
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
 * JcrDB
 */
SetupWizard.validateStep4 = function() {

  if(SetupWizard.DEBUG_MODE) {
    SetupWizard.showStep(5);
    return;
  }

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
  
  SetupWizard.showStep(6);
}

/**
 * IdmDb
 */
SetupWizard.validateStep6 = function() {

  if(SetupWizard.DEBUG_MODE) {
    SetupWizard.showStep(7);
    return;
  }

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

  if(SetupWizard.DEBUG_MODE) {
    SetupWizard.showStep(8);
    return;
  }
  
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

  if(SetupWizard.DEBUG_MODE) {
    SetupWizard.beforeStep9();
    return;
  }
  
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
  
  // Call method to init next step
  SetupWizard.beforeStep9();
}

/**
 * This method needs to be called just before step9, init step summary with all values entered by user during setup wizard
 */
SetupWizard.beforeStep9 = function() {

  // Begin to show step 9
  SetupWizard.showStep(9);
  
  // Lock screen
  SetupWizard.displayLoader(9);
  
  // fetch all inputs into hidden form
  var inputs = $('#SetupExitForm input');
  
  for(var i=0; i<inputs.length; i++) {
    SetupWizard.writeNewRow('SummaryTable', SetupWizard.LABELS_PP[inputs[i].name], inputs[i].value);
  }
  
  // Unlock screen
  SetupWizard.hideLoader(9);
}

/**
 * Summary
 */
SetupWizard.validateStep9 = function() {

  if(SetupWizard.DEBUG_MODE) {
    SetupWizard.showStep(10);
    return;
  }

  // display loader
  SetupWizard.displayLoader(9);
  
  // Validate Form with WS writeProperties, if response is ok, call WS startPlatform and display next screen
  $.post(
    "/setup/setuprest/service/wp", 
    $("#SetupExitForm").serialize(),
    function(data) {}, 
    "json"
  )
  .complete(function(data) {
    SetupWizard.hideLoader(9);
    if(data.responseText == "ok") {
      SetupWizard.displayLoader(9);
      $.ajax({
        url: "/setup/setuprest/service/sp"
      })
      .always(function (data) {
        SetupWizard.hideLoader(9);
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
  
  SetupWizard.showStep(1);
  // TODO redirect to portal home page
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

/**
 * Write new row with 2 tds from table with ID #tableId
 * if line is yet existing, it is not added twice.
 */
SetupWizard.writeNewRow = function(tableId, td1, td2) {
  var elem = $('#' + tableId + ' tbody>tr:last');
  var newRow = $("<tr><td><strong>" + td1 + "</strong></td><td>" + td2 + "</td></tr>");
  
  // Verify existence of row
  var trs = $('#' + tableId + ' tr');
  var isExisting = false;
  for(var i=0; i<trs.length; i++) {
    // HACK (IE7, IE8) dom element returned by these navigators are in LOWER case (need to get only text) PLF-3674
    var lineTd1 = $(trs[i].children[0]).text();
    var lineTd2 = $(trs[i].children[1]).text();
    if(td1 == lineTd1 && td2 == lineTd2) {
      isExisting = true;
      break;
    }
    if(td1 == lineTd1){
      $(trs[i].children[1]).html(td2);
      isExisting = true;
      break;
    }
  }
  
  // Add row
  if(! isExisting) {
    if(elem != undefined && elem.length > 0) {
      elem.after(newRow);
    }
    else {
      $('#' + tableId).append(newRow);
    }
  }
}

/**
 * This method returns a boolean value
 */
SetupWizard.getBoolean = function(value) {
  return (typeof(value)=='string') ? ($.trim(value) == "true" || $.trim(value) == "TRUE") : Boolean(value);
}
