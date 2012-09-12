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
                <input type="radio" name="radioIdmDs" id="radioIdmDs1" value="optionIdm1" checked="">
                Choose a datasource:
              </label>
            </div>
          </div>
          <div class="span2">
            <div class="form-inline">
              <select id="selectIdmDs">
              </select>
            </div>
          </div>
        </div>
        
        <div class="row">
          <div class="span2">
            <div class="form-inline">
              <label class="radio">
                <input type="radio" name="radioIdmDs" id="radioIdmDs2" value="optionIdm2">
                Set your own datasource:
              </label>
            </div>
          </div>
          <div class="span2">
            <div class="form-inline">
              <input type="text" class="input-xmedium" id="inputIdmDs">
            </div>
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
                <div class="bar" style="width: 75%;"></div>
              </div>
            </td>
            <td width="30%">
              <input class="btn" type="submit" value="Previous" onclick="SetupWizard.showStep(5);">
              <input class="btn btn-primary" type="submit" value="Next" onclick="SetupWizard.validateStep6();">
            </td>
          </tr>
        </table>
      </div>
    </div>