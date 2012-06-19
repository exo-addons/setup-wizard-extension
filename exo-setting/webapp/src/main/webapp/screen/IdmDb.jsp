    <div class="IdmDb">
      <h3 class="TitleBar">IDM Database setup</h3>
      <div class="Content">
        <p class="LabelCT">The database will be used to host the IDM</p>
        
        <div class="alert alert-info">
          <button class="close" data-dismiss="alert">×</button>
          <strong>Warning!</strong> You need to install your database driver
        </div>
        
        <div class="row">
          <div class="span2">
            <div class="form-inline">
              <label class="radio">
                <input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" checked="">
                Choose a datasource:
              </label>
            </div>
          </div>
          <div class="span2">
            <div class="form-inline">
              <select id="select03">
                <option>Ds1</option>
                <option>Ds2</option>
              </select>
            </div>
          </div>
        </div>
        
        <div class="row">
          <div class="span2">
            <div class="form-inline">
              <label class="radio">
                <input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" checked="">
                Set your own datasource:
              </label>
            </div>
          </div>
          <div class="span2">
            <div class="form-inline">
              <input type="text" class="input-xmedium" id="input01">
            </div>
          </div>
        </div>
        
        <div class="row">
          <div class="span4">
            <div class="form-inline">
              <label class="checkbox">
                <input type="checkbox" id="optionsCheckbox" value="option1">
                Store files in database
              </label>
            </div>
          </div>
        </div>
          
      </div>
      
      <div class="ButtonContent">
        <div class="row">
          <div class="span4">
            <div class="progress progress-striped">
              <div class="bar" style="width: 75%;"></div>
            </div>
          </div>
          <div class="span2">
            <input class="btn" type="submit" value="Previous" onclick="SetupWizard.showStep(5);">
            <input class="btn btn-primary" type="submit" value="Next" onclick="SetupWizard.validateStep6();">
          </div>
        </div>
      </div>
    </div>