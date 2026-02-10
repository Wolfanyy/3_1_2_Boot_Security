package boot_security.service;

import boot_security.model.Role;
import boot_security.model.RoleName;


public interface RoleService {
    Role getOrCreateRole(RoleName roleName);
}
