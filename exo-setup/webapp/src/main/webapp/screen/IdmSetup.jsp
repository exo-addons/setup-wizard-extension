    <div class="IdmSetup">
      <h3 class="TitleBar">IDM Setup</h3>
      <div class="Content">
        <p class="LabelCT">IDM (Organization model)</p>
        <fieldset>
          <div class="control-group">
            <div class="controls">
              <label class="radio">
                <input type="radio" name="radioIdmSetup" id="radioIdmSetup1" value="database" checked="" />
                <strong>Database</strong>
              </label>
              <label class="radio">
                <input type="radio" name="radioIdmSetup" id="radioIdmSetup2" value="ldap" />
                <strong>LDAP</strong>
              </label>
            </div>
          </div>
        </fieldset>
      </div>
      
      <div class="ButtonContent">
        <table width="100%">
          <tr>
            <td width="70%">
              <div class="progress progress-striped">
                <div class="bar" style="width: 62.5%;"></div>
              </div>
            </td>
            <td width="30%">
              <input class="btn" type="submit" value="Previous" onclick="SetupWizard.showStep(4);">
              <input class="btn btn-primary" type="submit" value="Next" onclick="SetupWizard.validateStep5();">
            </td>
          </tr>
        </table>
      </div>
    </div>