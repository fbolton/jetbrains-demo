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


                // get project Id
                val id = api.projectId()
                // get current build run number
                val runNumber = api.executionNumber()

                //get all issue statuses
                val statuses = api.space().projects.planning.issues.statuses.
                getAllIssueStatuses(project = ProjectIdentifier.Id(id))
                //get id of 'Open' issue status
                val openStatusId = statuses.find { it.name == "Open" }?.id
                    ?: throw kotlin.Exception("The 'Open' state doesn't exist in the project")
                // create issue with 'Open' status
                api.space().projects.planning.issues.createIssue(
                    project = ProjectIdentifier.Id(id),
                    // generate name based on build run number
                    title = "Job 'Build and publish' #$runNumber failed",
                    description = "${ex.message}",
                    status = openStatusId
                )
            }
        }
    }
}