    <div class="IdmSetup">
      <h3 class="TitleBar">IDM Setup</h3>
      <div class="Content">
        <p class="LabelCT">IDM (Organization model)</p>
        <fieldset>
          <div class="control-group">
            <div class="controls">
              <label class="radio">
                <input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" checked="" />
                <strong>Database</strong>
              </label>
              <label class="radio">
                <input type="radio" name="optionsRadios" id="optionsRadios2" value="option2" />
                <strong>LDAP</strong>
              </label>
            </div>
          </div>
        </fieldset>
      </div>
      
      <div class="ButtonContent">
        <div class="row">
          <div class="span4">
            <div class="progress progress-striped">
              <div class="bar" style="width: 62.5%;"></div>
            </div>
          </div>
          <div class="span2">
            <input class="btn" type="submit" value="Previous" onclick="SetupWizard.showStep(4);">
            <input class="btn btn-primary" type="submit" value="Next" onclick="SetupWizard.validateStep5();">
          </div>
        </div>
      </div>
    </div>