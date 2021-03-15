package me.nerdoron.swissbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.cdimascio.dotenv.Dotenv;
import me.nerdoron.swissbot.commandmanager.CommandManager;
import me.nerdoron.swissbot.commands.askstaff.AnswerCommand;
import me.nerdoron.swissbot.commands.askstaff.AskStaffCommand;
import me.nerdoron.swissbot.commands.askstaff.RetractCommand;
import me.nerdoron.swissbot.commands.global.PingCommand;
import me.nerdoron.swissbot.commands.global.fun.EightBallCommand;
import me.nerdoron.swissbot.commands.global.fun.JokeCommand;
import me.nerdoron.swissbot.global.afksystem.AFKCommand;
import me.nerdoron.swissbot.global.afksystem.AFKMessageEvent;
import me.nerdoron.swissbot.utils.tests.DbTest;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {
	
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	private static String prefix = "+";
	
	
	public static void main(String[] args) throws IllegalArgumentException {
		try {
			logger.info("Trying to setup the bot...");
			logger.info("Trying to setup load env file...");
			dotenv();
		} catch (Exception ex) {
			logger.error("An exception occurred while trying to setup the bot!", ex);
		}
	}
	
	
	private static void dotenv() {
		try {
			Dotenv dotenv = Dotenv.load();
			logger.info("Loaded env file!");
			logger.info("Trying to load the bot...");
			bot(dotenv);
		} catch (Exception ex) {
			logger.error("An exception occurred while trying to load the dotenv file!", ex);
		}
	}
	

	private static void bot(Dotenv dotenv) {
		String token = dotenv.get("TOKEN");
		try {
			JDA api = JDABuilder.createDefault(token).enableIntents(
					GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS
					)
					.build();
			api.getPresence().setActivity(Activity.playing("nerdobot v0.1 => https://github.com/Swiss001-Development/nerdobot"));
			logger.info("Bot loaded!");
			logger.info("Trying to register commands...");
			registerCommand(api);
		} catch (Exception ex) {
			logger.error("An exception occurred while trying to load the bot!", ex);
		}
	}
	
	private static void registerCommand(JDA jda) {
			new CommandManager(jda, prefix)
			.registerCommand(new DbTest(), "dbtest")
			.registerCommand(new PingCommand(), "ping", "pong")
			.registerCommand(new AFKCommand(), "afk")
			.registerCommand(new AskStaffCommand(), "ask-staff", "askstaff")
			.registerCommand(new AnswerCommand(), "answer")
			.registerCommand(new RetractCommand(), "retract")
			.registerCommand(new JokeCommand(), "joke", "pun", "dadjoke")
			.registerCommand(new EightBallCommand(), "8ball", "eightball");
			logger.info("Loaded commands!");
			logger.info("Trying to register events...");
			hookEvents(jda);
	}
	
	private static void hookEvents(JDA jda)  {
		jda.addEventListener(new AFKMessageEvent());
		logger.info("Loaded events!");
	}
	
	

}
