    <div class="SetupType">
      <h3 class="TitleBar">Select a setup type</h3>
      <div class="Content">
        <div>
          <div>
            <fieldset>
              <div class="control-group">
                <div class="controls">
                  <label class="radio">
                    <input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" checked="">
                    <strong>Standard</strong> (Recommended)<br />
                    Display standard options that most administrators have to
                  </label>
                  <label class="radio">
                    <input type="radio" name="optionsRadios" id="optionsRadios2" value="option2">
                    <strong>Advanced</strong><br />
                    Display All advanced options, like JCR cache & indexer
                  </label>
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
              <div class="bar" style="width: 25%;"></div>
              </div>
            </td>
            <td width="30%">
            <input class="btn" type="submit" value="Previous" onclick="SetupWizard.showStep(1);">
            <input class="btn btn-primary" type="submit" value="Next" onclick="SetupWizard.validateStep2();">
            </td>
          </tr>
        </table>
      </div>
    </div>