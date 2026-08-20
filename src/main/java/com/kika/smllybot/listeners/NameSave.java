package com.kika.smllybot.listeners;

import com.kika.smllybot.database.sql.users.UsersTable;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class NameSave extends ListenerAdapter {

    @Override
    public void onUserUpdateName(@NotNull UserUpdateNameEvent event) {
        long id = event.getUser().getIdLong();
        String username = event.getNewName();

        UsersTable.setUsername(id, username);
    }
}
