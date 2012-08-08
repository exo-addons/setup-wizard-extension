/**
 * Module used to manage Setup wizard JS features
 *
 */
var SetupWizard = {};

SetupWizard.NB_SCREENS = 10;

SetupWizard.initSetupWizard = function() {
  if (!window.console) console = {};
  console.log = console.log || function(){};
  console.warn = console.warn || function(){};
  console.error = console.error || function(){};
  console.info = console.info || function(){};
}

SetupWizard.showStep = function(n) {
  for(var i=1; i<=SetupWizard.NB_SCREENS; i++) {
    $('#setup' + i).hide();
  }
  $('#setup' + n).show();
}

SetupWizard.validateStep1 = function(event) {
  SetupWizard.showStep(2);
}

SetupWizard.validateStep2 = function() {
  SetupWizard.showStep(3);
}

SetupWizard.validateStep3 = function() {
  SetupWizard.showStep(4);
}

SetupWizard.validateStep4 = function() {
  SetupWizard.showStep(5);
}

SetupWizard.validateStep5 = function() {
  SetupWizard.showStep(6);
}

SetupWizard.validateStep6 = function() {
  SetupWizard.showStep(7);
}

SetupWizard.validateStep7 = function() {
  SetupWizard.showStep(8);
}

SetupWizard.validateStep8 = function() {
  SetupWizard.showStep(9);
}

SetupWizard.validateStep9 = function() {
  SetupWizard.showStep(10);
}

SetupWizard.validateStep10 = function() {
  SetupWizard.exit();
}

SetupWizard.exit = function() {
  $("#SetupExitForm").submit();
}
