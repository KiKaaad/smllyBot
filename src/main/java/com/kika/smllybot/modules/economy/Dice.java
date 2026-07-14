package com.kika.smllybot.modules.economy;

import com.kika.smllybot.other.BaseCmd;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.container.ContainerChildComponent;
import net.dv8tion.jda.api.components.mediagallery.MediaGallery;
import net.dv8tion.jda.api.components.mediagallery.MediaGalleryItem;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.InputStream;
import java.util.Set;

public class Dice extends BaseCmd {

    public Dice() {
        super(Set.of("кубик", "dice"));
    }

    InputStream stream = getClass().getResourceAsStream("/images/five.gif");

    @Override
    public Container execute(MessageReceivedEvent event, String args) {

        FileUpload upload = FileUpload.fromData(stream, "images/five.gif");
        ContainerChildComponent response = MediaGallery.of(
                MediaGalleryItem.fromFile(upload)
        );

        event.getChannel().sendMessageComponents(Container.of(response))
                .useComponentsV2(true)
                .queue();

        return null;
    }
}
