    <div class="JcrDb">
      <h3 class="TitleBar">JCR Database setup</h3>
      <div class="Content">
        <p class="LabelCT">The database will be used to host the JCR</p>
        
        <div class="alert alert-info">
          <button class="close" data-dismiss="alert">×</button>
          <strong>Warning!</strong> You need to install your database driver
        </div>
        
        <div class="row">
          <div class="span2">
            <div class="form-inline">
              <label class="radio">
                <input type="radio" name="optionsJcrRadios" id="radioJcrDs" value="optionJcr1" checked="">
                Choose a datasource:
              </label>
            </div>
          </div>
          <div class="span2">
            <div class="form-inline">
              <select id="selectJcrDs">
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
                <input type="radio" name="optionsJcrRadios" id="radioJcrOwnDs" value="optionJcr2">
                Set your own datasource:
              </label>
            </div>
          </div>
          <div class="span2">
            <div class="form-inline">
              <input type="text" class="input-xmedium" id="inputJcrDs">
            </div>
          </div>
        </div>
        
        <div class="row">
          <div class="span4">
            <div class="form-inline">
              <label class="checkbox">
                <input type="checkbox" id="checkboxStore" value="optionJcr1">
                Store files in database
              </label>
            </div>
          </div>
        </div>
          
      </div>
      
      <div class="ButtonContent">
        <table width="100%">
          <tr>
            <td width="70%">
              <div class="progress progress-striped">
                <div class="bar" style="width: 50%;"></div>
              </div>
            </td>
            <td width="30%">
            <input class="btn" type="submit" value="Previous" onclick="SetupWizard.showStep(3);">
            <input class="btn btn-primary" type="submit" value="Next" onclick="SetupWizard.validateStep4();">
            </td>
          </tr>
        </table>
      </div>
    </div>