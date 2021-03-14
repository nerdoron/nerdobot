package me.nerdoron.nerdobot.commandmanager;


import java.sql.Timestamp;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.cdimascio.dotenv.Dotenv;
import me.nerdoron.nerdobot.utils.Global;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Command {
	
	public void executeGlobal(MessageReceivedEvent commandEvent, String cmd, Object[] args) {
		final Logger logger = LoggerFactory.getLogger(Command.class);
		Dotenv dotenv = Dotenv.load();
    	try {
        execute(commandEvent, args, logger, dotenv);
    	} catch (Exception ex ) {
    		Global.errorEmbed(commandEvent);
    		logger.error("Error while tryng to execute command " + commandEvent.getMessage().getContentRaw() + "\n\n", ex);
    		String error = ExceptionUtils.getStackTrace(ex);
    		TextChannel channel = commandEvent.getJDA().getTextChannelById(dotenv.get("DEBUG_ID"));
    		channel.sendMessage("<@221204198287605770> Error detected!").queue();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            MessageEmbed errorEmbed = new EmbedBuilder()
                    .setTitle("Error!")
                    .addField("Cause", String.valueOf(error), false)
                    .addField("Command Used", commandEvent.getMessage().getContentRaw(), false)
                    .addField("Guild", commandEvent.getGuild().getName() + "\n" + commandEvent.getGuild().getId(), false)
                    .addField("Channel", commandEvent.getChannel().getName() + "\n" + commandEvent.getChannel().getId(), false)
                    .addField("Time", String.valueOf(timestamp), false)
                    .build();
            channel.sendMessage(errorEmbed).queue();
    	}
	}
	
    public abstract void execute(MessageReceivedEvent commandEvent, Object[] args, Logger logger, Dotenv dotenv);

	
	
}