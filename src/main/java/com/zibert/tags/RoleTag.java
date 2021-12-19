package com.zibert.tags;

import com.zibert.DAO.entity.User;

import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * Custom tag printing the current user's role
 */

public class RoleTag extends SimpleTagSupport {

    User user;

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void doTag() throws IOException {
        int roleId = user.getRoleId();
        if (roleId == 1) {
            getJspContext().getOut().print("admin");
        }
        if (roleId == 2) {
            getJspContext().getOut().print("manager");
        }
        if (roleId == 3) {
            getJspContext().getOut().print("user");
        }
    }
}
