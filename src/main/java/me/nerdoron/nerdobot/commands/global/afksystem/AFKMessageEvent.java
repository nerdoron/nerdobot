package me.nerdoron.nerdobot.commands.global.afksystem;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.nerdoron.nerdobot.utils.Database;
import me.nerdoron.nerdobot.utils.Global;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AFKMessageEvent extends ListenerAdapter {

	static final Logger logger = LoggerFactory.getLogger(AFKMessageEvent.class);

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String uid = event.getMember().getId();
		boolean afk = AFKChecks.CheckAFK(uid);
		Connection con = Database.connect();
		PreparedStatement ps = null;

		// remove afk status if sends message
		if ((afk == true) && (!event.getMessage().getContentRaw().startsWith("+afk"))) {
			try {
				String SQL = "delete from afk where uid=?";
				ps = con.prepareStatement(SQL);
				ps.setString(1, uid);
				ps.execute();
				MessageEmbed back = new EmbedBuilder().setTitle(":wave: Welcome back, " + event.getAuthor().getAsTag())
						.setDescription("I've removed your AFK status!").setColor(Global.embedColor).build();
				event.getChannel().sendMessage(back).queue();
			} catch (Exception ex) {
				logger.error(ex.toString());
			}
		}
		
		// notify if afk
        List<Member> memberList = event.getMessage().getMentionedMembers();
        
        for(int i = 0; i < memberList.size(); i++) {
        	Member target = memberList.get(i);
        	String targetUid = target.getId();
    		boolean targetAfk = AFKChecks.CheckAFK(targetUid);
    		if(targetAfk == true) {
    			MessageEmbed isAfk = new EmbedBuilder()
    					.setTitle(":no_entry: " + target.getEffectiveName() + " is AFK!")
    					.setDescription(target.getEffectiveName() + " is AFK, they will see your message when they get back.")
    					.addField("Reason", AFKChecks.afkReason(targetUid), false)
    					.setColor(Color.red)
    					.build();
    					event.getChannel().sendMessage(isAfk).queue();
    		}
        }

	}

}
