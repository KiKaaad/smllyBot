package com.kika.smllybot.listeners;

import com.kika.smllybot.database.sql.user.UserTable;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class NameSave extends ListenerAdapter {

    @Override
    public void onUserUpdateName(@NotNull UserUpdateNameEvent event) {
        long id = event.getUser().getIdLong();
        String username = event.getNewName();

        UserTable.updateUsername(id, username);
    }
}
