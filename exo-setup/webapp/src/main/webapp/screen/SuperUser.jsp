    <div class="SuperUser">
      <h3 class="TitleBar">Super user</h3>
      <div class="Content">
        <p class="LabelCT">The super user will have all privileges on your portal</p>
        <div>
          <div class="form-horizontal">
            <fieldset>
              <div class="control-group">
                <label class="control-label" for="inputUsername">Username</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="inputUsername">
                </div>
              </div>
              <div class="control-group">
                <label class="control-label" for="inputPassword">Password</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="inputPassword">
                </div>
              </div>
              <div class="control-group">
                <label class="control-label" for="inputConfirmPassword">Confirm password</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="inputConfirmPassword">
                </div>
              </div>
              <div class="control-group">
                <label class="control-label" for="inputEmail">Email</label>
                <div class="controls">
                  <input type="text" class="input-xlarge" id="inputEmail">
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