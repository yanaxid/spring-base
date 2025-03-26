package com.base.basicsecurity.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
   USER("user role / member"),
   ADMIN("administrator");

   private final String displayName;

   Role(String displayName) {
      this.displayName = displayName;
   }

   public String getDisplayName() {
      return displayName;
   }

   @Override
   public String getAuthority() {
      return "ROLE_" + name();
   }
}
