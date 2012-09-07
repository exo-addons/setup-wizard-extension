    <div class="Website">
      <h3 class="TitleBar">Install a Website</h3>
      <div class="Content">
        <p class="LabelCT">Install a website to your portal. You can choose a ready-made sample or start fresh with a blank website.</p>

        <div class="row">
          <div class="span5">
            <div class="form-inline">
              <label class="radio">
                <input type="radio" name="radioWebsite" id="radioWebsiteBlank" value="radioWebsite1" checked="">
                Start with a blank portal
              </label>
            </div>
          </div>
        </div>
        
        <div class="row">
          <div class="span2">
            <div class="form-inline">
              <label class="radio">
                <input type="radio" name="radioWebsite" id="radioWebsiteInstall" value="radioWebsite2">
                Install a sample
              </label>
            </div>
          </div>
          <div class="span3">
            <div class="form-horizontal">
              <label class="checkbox">
                <input type="checkbox" name="optionsWebSite" value="acme">
                ACME
              </label>
              <label class="checkbox">
                <input type="checkbox" name="optionsWebSite" value="default">
                Default
              </label>
              <label class="checkbox">
                <input type="checkbox" name="optionsWebSite" value="intranet">
                Intranet
              </label>
            </div>
          </div>
        </div>
        
        <div class="row website">
          <div class="span2">
            <img class="Image" alt="" src="background/img1.png" width="171">
          </div>
          <div class="span3">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer elementum, ipsum accumsan vehicula bibendum, tellus eros ultrices libero, id viverra risus lacus
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
              <div class="bar" style="width: 100%;"></div>
              </div>
            </td>
            <td width="30%">
            <input class="btn" type="submit" value="Previous" onclick="SetupWizard.showStep(7);">
            <input id="toto" class="btn btn-primary" type="submit" value="Next" onclick="SetupWizard.validateStep8();">
            </td>
          </tr>
        </table>
      </div>
    </div>