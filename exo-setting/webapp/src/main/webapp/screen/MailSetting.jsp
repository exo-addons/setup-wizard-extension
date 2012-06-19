    <div class="MailSetting">
      <h3 class="TitleBar">Mail Setting</h3>
      <div class="Content">
        <p class="LabelCT">The mail settings are used by the portal to send notifications</p>
        <div>
          <div class="form-horizontal">
            <fieldset>
              <div class="control-group">
                <label class="control-label" for="control_6_1">SMTP Host</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="control_6_1">
                </div>
              </div>
              <div class="control-group">
                <label class="control-label" for="control_6_2">Port</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="control_6_2">
                </div>
              </div>
              <div class="control-group">
                <label class="control-label" for="optionsCheckbox">Secured connection</label>
                <div class="controls">
                  <label class="checkbox">
                    <input type="checkbox" id="optionsCheckbox" value="option6">
                  </label>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label" for="control_6_3">Username</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="control_6_3">
                </div>
              </div>
              <div class="control-group">
                <label class="control-label" for="control_6_4">Password</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="control_6_4">
                </div>
              </div>
              <div class="control-group">
                <label class="control-label" for="control_6_5">Email</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="control_6_5">
                </div>
              </div>
            </fieldset>
          </div>
        </div>
      </div>
      
      <div class="ButtonContent">
        <div class="row">
          <div class="span4">
            <div class="progress progress-striped">
              <div class="bar" style="width: 87.5%;"></div>
            </div>
          </div>
          <div class="span2">
            <input class="btn" type="submit" value="Previous" onclick="SetupWizard.showStep(6);">
            <input class="btn btn-primary" type="submit" value="Next" onclick="SetupWizard.validateStep7();">
          </div>
        </div>
      </div>
    </div>