/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.builders;

import com.areg.project.models.entities.AccessControlEntity;
import com.areg.project.models.entities.DomainEntity;
import com.areg.project.models.entities.ObjectEntity;
import com.areg.project.models.entities.ObjectGroupEntity;
import com.areg.project.models.entities.PermissionEntity;
import com.areg.project.models.entities.UserEntity;
import com.areg.project.models.entities.UserGroupEntity;
import com.areg.project.services.implementations.AccessControlService;
import com.areg.project.services.implementations.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PermissionsWildcardBuilder {

    private final UserService userService;

    private final AccessControlService accessControlService;

    @Autowired
    public PermissionsWildcardBuilder(UserService userService, AccessControlService accessControlService) {
        this.userService = userService;
        this.accessControlService = accessControlService;
    }


    //  Build user permission wildcard by user email
    public Set<String> build(String email) {
        final UserEntity userById = userService.getActiveUserByEmail(email);
        final UserGroupEntity userGroup = userById.getUserGroup();
        if (userGroup == null) {
            return Collections.emptySet();
        }

        final AccessControlEntity accessControl = accessControlService.getByUserGroupId(userGroup);
        final Set<ObjectGroupEntity> objectGroupSet = accessControl.getObjectGroups();
        final Set<String> wildcards = new HashSet<>();

        for (var objectGroup : objectGroupSet) {
            if (! objectGroup.getObjects().isEmpty()) {
                final DomainEntity currentDomain = objectGroup.getObjects().stream().iterator().next().getDomain();
                wildcards.add(buildPermissionsWildcard(
                        currentDomain, objectGroup.getObjects(), accessControl.getRole().getPermissions()));
            }
        }
        return wildcards;
    }


    private String buildPermissionsWildcard(DomainEntity domain, Set<ObjectEntity> objects, Set<PermissionEntity> permissions) {
        List<PermissionEntity> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(permissions)) {
            result = permissions.stream()
                    .filter(perm -> perm.getDomain().equals(domain) && domain.getPermissions().contains(perm)).toList();
        }

        if (result.isEmpty()) {
            return "";
        }

        final String permissionsString = result.stream().map(perm -> perm.getName() + ",").collect(Collectors.joining());
        final String objectsString = objects.stream().map(obj -> obj.getUuid().toString() + ",").collect(Collectors.joining());

        return domain.getCode() + ":" + StringUtils.chop(permissionsString) + ":" + StringUtils.chop(objectsString);
    }
}
