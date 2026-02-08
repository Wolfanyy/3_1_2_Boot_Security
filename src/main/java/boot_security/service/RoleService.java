package boot_security.service;

import boot_security.model.RoleName;


public interface RoleService {
    void ensureRoleExists(RoleName roleName);
}
