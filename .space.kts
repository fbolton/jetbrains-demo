job("Build and publish") {
    container(displayName = "Build and notify", image = "gradle:7.2.0-jdk11") {
        kotlinScript { api ->
            val channel = ChannelIdentifier.Channel(ChatChannel.FromName("developers"))
            try {
                api.gradlew("build")
                val content = ChatMessage.Text("Build succeeded")
                api.space().chats.messages.sendMessage(channel = channel, content = content)
            } catch (ex: Exception) {
                val content = ChatMessage.Text("Build failed")
                api.space().chats.messages.sendMessage(channel = channel, content = content)

                // get current build run number
                val runNumber = api.executionNumber()
                val content = ChatMessage.Text("Job 'Build and publish' #$runNumber failed")
                api.space().chats.messages.sendMessage(channel = channel, content = content)
            }
        }
    }
}