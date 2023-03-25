package Discord.HashingBot.Command;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandManeger extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equals("welcome")) {
            // Run the 'ping' command
            String userTag = event.getUser().getAsTag();
            event.reply("Welcome to the server, **" + userTag + "**!").setEphemeral(true).queue();
        }
        else if (command.equals("roles")) {
            // run the 'roles' command
            event.deferReply().setEphemeral(true).queue();
            String response = "";
            for (Role role : event.getGuild().getRoles()) {
                response += role.getAsMention() + "\n";
            }
            event.getHook().sendMessage(response).queue();
            //リプライ限定機能　true = 隠す / false = 見せる
        }
        else if (command.equals("say")) {
            // Get message option
            OptionMapping messageOption = event.getOption("message");
            String message = messageOption.getAsString();
            MessageChannel channel;
            OptionMapping channelOption = event.getOption("channel");
            if (channelOption != null) {
                channel = channelOption.getAsMessageChannel();
            } else {
                channel = event.getChannel();
            }
            channel.sendMessage(message).queue();
            event.reply("Your message was sent!").setEphemeral(true).queue();
        }
        else if (command.equals("emote")) {
            OptionMapping option = event.getOption("type");
            String type = option.getAsString();

            String replyMessage = "";
            switch (type.toLowerCase()) {
                case "hug" -> {
                    replyMessage = "You hug the closest person to you.";
                }
                case "laugh" -> {
                    replyMessage = "You laugh hysterically at everyone around you.";
                }
                case "cry" -> {
                    replyMessage = "You can't stop crying";
                }
            }
            event.reply(replyMessage).queue();
        }
        else if (command.equals("giverole")) {
            Member member = event.getOption("user").getAsMember();
            Role role = event.getOption("role").getAsRole();
            event.getGuild().addRoleToMember(member, role).queue();
            event.reply(member.getAsMention() + " has been given the " + role.getAsMention() + " role!").queue();
        }
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("welcome", "Get welcomed by the bot"));
        commandData.add(Commands.slash("roles", "Display all roles on the server"));
        OptionData option1 = new OptionData(OptionType.STRING, "message", "The message you want the bot to say", true);//true=必須?
        OptionData option2 = new OptionData(OptionType.CHANNEL, "channel", "The channel you want to send this message in")
                .setChannelTypes(ChannelType.TEXT, ChannelType.NEWS, ChannelType.GUILD_PUBLIC_THREAD);
        commandData.add(Commands.slash("say", "Make the bot say a message").addOptions(option1, option2));
        OptionData option3 = new OptionData(OptionType.STRING, "type", "The type of emotion to express", true)
                .addChoice("Hug", "hug")
                .addChoice("Laugh", "laugh")
                .addChoice("Cry", "cry");
        commandData.add(Commands.slash("emote", "Express your emotions through text.").addOptions(option3));
        OptionData option4 = new OptionData(OptionType.USER, "user", "The user to give the role to", true);
        OptionData option5 = new OptionData(OptionType.ROLE, "role", "The role to be given", true);
        commandData.add(Commands.slash("giverole", "Give a user a role").addOptions(option4, option5));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}
