    <div class="SuperUser">
      <h3 class="TitleBar">Super user</h3>
      <div class="Content">
        <p class="LabelCT">The super user will have all privileges on your portal</p>
        <div>
          <div class="form-horizontal">
            <fieldset>
              <div class="control-group">
                <label class="control-label" for="control_3_1">Username</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="control_3_1">
                </div>
              </div>
              <div class="control-group">
                <label class="control-label" for="control_3_2">Password</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="control_3_2">
                </div>
              </div>
              <div class="control-group">
                <label class="control-label" for="control_3_3">Confirm password</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="control_3_3">
                </div>
              </div>
              <div class="control-group">
                <label class="control-label" for="control_3_4">Email</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="control_3_4">
                </div>
              </div>
            </fieldset>
          </div>
        </div>
      </div>
      
      <div class="ButtonContent">
        <table width="100%">
          <tr>
            <td width="70%">
              <div class="progress progress-striped">
                <div class="bar" style="width: 37.5%;"></div>
              </div>
            </td>
            <td width="30%">
            <input class="btn" type="submit" value="Previous" onclick="SetupWizard.showStep(2);">
            <input class="btn btn-primary" type="submit" value="Next" onclick="SetupWizard.validateStep3();">
            </td>
          </tr>
        </table>
      </div>
    </div>