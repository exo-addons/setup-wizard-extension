    <div class="Setup">
      <h3 class="TitleBar">Setup</h3>
      <div class="Content">
        <p class="LabelCT">We have detected the following environment on your server</p>
        <div class="BlockTable">
          <table class="table table-striped" id ="SystemPropertiesTable">
          </table>
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
                <div class="bar" style="width: 12.5%;"></div>
              </div>
            </td>
            <td width="30%">
              <input class="btn btn-inverse" type="submit" value="Skip wizard" onclick="SetupWizard.exit();">
              <input class="btn btn-primary" type="submit" value="Start" onclick="SetupWizard.validateStep1();" >
            </td>
          </tr>
        </table> 
      </div>
    </div>