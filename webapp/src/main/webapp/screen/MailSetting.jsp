    <div class="MailSetting">
      <h3 class="TitleBar">Mail Setting</h3>
      <div class="Content">
        <p class="LabelCT">The mail settings are used by the portal to send notifications</p>
        <div>
          <div class="form-horizontal">
            <fieldset>
              <div class="control-group">
                <label class="control-label" for="inputMailSmtpHost">
                  SMTP Host
                  &nbsp;<span style="color:red; font-weight: bold;">*</span>
                </label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="inputMailSmtpHost">
                </div>
              </div>
              <div class="control-group">
                <label class="control-label" for="inputMailPort">
                  Port
                  &nbsp;<span style="color:red; font-weight: bold;">*</span>
                </label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="inputMailPort">
                </div>
              </div>
              <div class="control-group">
                <label class="control-label" for="checkboxMailSecure">
                Secured connection
                  &nbsp;<span style="color:red; font-weight: bold;">*</span>
                </label>
                <div class="controls">
                  <label class="checkbox">
                    <input type="checkbox" id="checkboxMailSecure" value="secured_connection">
                  </label>
                </div>
              </div>
              <div class="control-group">
                <label class="control-label" for="inputMailUserName">
                  Username
                  &nbsp;<span style="color:red; font-weight: bold;">*</span>
                </label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="inputMailUserName">
                </div>
              </div>
              <div class="control-group">
                <label class="control-label" for="inputMailPassword">
                  Password
                  &nbsp;<span style="color:red; font-weight: bold;">*</span>
                </label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="inputMailPassword">
                </div>
              </div>
              <div class="control-group">
                <label class="control-label" for="inputMailEmail">
                  Email
                  &nbsp;<span style="color:red; font-weight: bold;">*</span>
                </label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="inputMailEmail">
                </div>
              </div>
            </fieldset>
          </div>
        </div>
      </div>
      
      <div class="ButtonContent">
        <table width="100%">
          <tr>
            <td width="4%">
              <div class="loader_ctr">
                <div class="loader"></div>
              </div>
            </td>
            <td width="66%">
              <div class="progress progress-striped">
              <div class="bar" style="width: 87.5%;"></div>
              </div>
            </td>
            <td width="30%">
            <input class="btn" type="submit" value="Previous" onclick="SetupWizard.showStep(6);">
            <input class="btn btn-primary" type="submit" value="Next" onclick="SetupWizard.validateStep7();">
            </td>
          </tr>
        </table>
      </div>
    </div>