package com.sherpa.wmail

import kotlin.random.Random

class EmailFetcher {
    companion object {
        val senders = listOf("Dahlia Cline", "Kevin Miranda", "Kaya Austin", "Laila Calderon", "Marquise Rhodes", "Fletcher Patel", "Luz Barron", "Kamren Dudley", "Jairo Foster", "Lilah Sandoval", "Ansley Blake", "Slade Sawyer", "Jaelyn Holmes", "Phoenix Bright", "Ernesto Gould")
        val titles = listOf("Quick Update", "Insights to Share", "Your Weekly Brief", "Exciting News!", "A Brief Overview", "Key Highlights", "Important Notes", "Let’s Connect", "What’s New?", "Highlights You Don't Want to Miss", "Thoughts for Today", "Catch Up!", "In Case You Missed It", "A Quick Reminder", "Stay Informed")
        val summaries = listOf(
            "Weekly Summary: Here’s a recap of the key events from the past week. Stay updated on what you might have missed.",
            "Monthly Wrap-Up: This month has been filled with significant milestones. Let’s review the highlights and achievements.",
            "Quarterly Insights: As we conclude this quarter, it’s important to reflect on our progress. Here are the major insights we've gained.",
            "Highlights of the Week: Check out the standout moments from this week. These updates will keep you in the loop.",
            "End-of-Month Review: It’s time to assess our accomplishments for the month. Here are the essential takeaways.",
            "Yearly Overview: As we look back on the year, let’s celebrate our successes. This overview captures our key moments.",
            "Key Takeaways: Here are the most important lessons learned recently. These insights can guide our future efforts.",
            "Summary of Activities: This document summarizes all the activities conducted. It's a quick way to catch up on our efforts.",
            "Latest Developments: Discover the newest changes and updates in our projects. Staying informed is crucial for our success.",
            "Summary Report: This report consolidates our findings and progress. It serves as a valuable resource for all stakeholders.",
            "Snapshot of Progress: Here’s a brief look at our advancements so far. It highlights what we’ve achieved together.",
            "Current Highlights: This section features the most exciting news of the moment. Stay tuned for what’s coming next.",
            "Concise Recap: Let’s summarize the key points for quick reference. This recap is designed for easy reading.",
            "Overview of Events: Here’s an overview of recent events that took place. It provides context for our ongoing initiatives.",
            "Highlights and Insights: This email captures both our achievements and key insights. Use this information to stay engaged with our mission."
        )

        fun getEmails(): MutableList<Email> {
            var emails : MutableList<Email> = ArrayList()
            val imgId = R.drawable.profileimg
            for (i in 0..9) {
                val email = Email(senders[i], titles[Random.nextInt(0, titles.size)], summaries[Random.nextInt(0, summaries.size)], "${Random.nextInt(1,12)}/${Random.nextInt(1,28)}/${Random.nextInt(1980,2025)}", picId = imgId, Random.nextInt(0, 5) < 3)
                emails.add(email)
            }
            return emails
        }

        fun getNext5Emails(): MutableList<Email> {
            var newEmails : MutableList<Email> = ArrayList()
            val imgId = R.drawable.profileimg
            for (i in 10..14) {
                val email = Email(senders[i], titles[Random.nextInt(0, titles.size)], summaries[Random.nextInt(0, summaries.size)], "${Random.nextInt(1,12)}/${Random.nextInt(1,28)}/${Random.nextInt(1980,2025)}", picId = imgId, Random.nextInt(0, 5) < 3
                )
                newEmails.add(email)
            }
            return newEmails
        }
    }
}