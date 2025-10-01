package dms.user.app;

import dms.admin.app.Backend;
import dms.admin.app.ResourceModel;


import java.util.List;
import java.util.ArrayList;


public class BackendAdapter extends Backend {
  private final BackendUser user;

  public BackendAdapter(BackendUser user) {
    super(user.getBase());
    this.user = user;
  }

  @Override
public dms.admin.app.IncidentModel getIncidentById(long id) throws Exception {
    for (dms.user.app.IncidentModel i : user.listIncidents()) {
        if (i.getId() == id) {
            dms.admin.app.IncidentModel adminModel = new dms.admin.app.IncidentModel();
            adminModel.setId(i.getId());
            adminModel.setDescription(i.getDescription());
            adminModel.setLat(i.getLat());
            adminModel.setLng(i.getLng());
            adminModel.setStatus(i.getStatus());
            adminModel.setType(i.getType());
            return adminModel;
        }
    }
    throw new Exception("Incident not found: " + id);
}

@Override
public List<dms.admin.app.ResourceModel> listResources() throws Exception {
    List<dms.user.app.ResourceModel> userResources = user.listResources();
    List<dms.admin.app.ResourceModel> adminResources = new ArrayList<>();
    for (dms.user.app.ResourceModel ur : userResources) {
        dms.admin.app.ResourceModel ar = new dms.admin.app.ResourceModel();
        ar.setId(ur.getId());
        ar.setType(ur.getType());
        ar.setLocation(ur.getLocation());
        ar.setDescription(ur.getDescription());
        ar.setQuantity(ur.getQuantity());
        ar.setIncidentId(ur.getIncidentId());
        // add other fields as needed
        adminResources.add(ar);
    }
    return adminResources;
}

}
