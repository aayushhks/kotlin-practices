package io.github.aayushhks.campuseventexplorer.data

/** The app's event collection. IDs are fixed so routes and list keys stay stable. */
object CampusEvents {

    val all: List<CampusEvent> = listOf(
        CampusEvent(
            id = 101L,
            title = "Mobile Development Meetup",
            category = "Tech",
            location = "CAS 214",
            time = "Tue, Oct 20, 6:30 PM",
            description = "Students building Android and iOS apps show what they are working on " +
                "and swap tips. Bring a laptop if you want feedback on your project. " +
                "Pizza is provided.",
        ),
        CampusEvent(
            id = 102L,
            title = "Resume Review Drop-In",
            category = "Career",
            location = "Questrom, Room 330",
            time = "Wed, Oct 21, 2:00 PM",
            description = "Career advisors review resumes one on one in 15 minute slots. " +
                "No appointment needed. Bring a printed copy or have a PDF ready on your phone.",
        ),
        CampusEvent(
            id = 103L,
            title = "Fall Jazz Ensemble Concert",
            category = "Arts",
            location = "Tsai Performance Center",
            time = "Thu, Oct 22, 7:30 PM",
            description = "The student jazz ensemble plays standards and a few original pieces " +
                "written by members. Free for students with a valid ID.",
        ),
        CampusEvent(
            id = 104L,
            title = "Intramural Soccer Final",
            category = "Sports",
            location = "Nickerson Field",
            time = "Sat, Oct 24, 11:00 AM",
            description = "The top two intramural teams play for the fall trophy. " +
                "Come cheer from the stands. The game moves indoors if it rains.",
        ),
        CampusEvent(
            id = 105L,
            title = "Software Engineering Internship Fair",
            category = "Career",
            location = "GSU Metcalf Ballroom",
            time = "Mon, Oct 26, 10:00 AM",
            description = "Over 30 companies recruiting for summer software, data and ML " +
                "internships. Business casual is recommended. Recruiters will collect " +
                "resumes at each table.",
        ),
        CampusEvent(
            id = 106L,
            title = "Machine Learning Reading Group",
            category = "Academic",
            location = "CDS 1646",
            time = "Tue, Oct 27, 5:00 PM",
            description = "A weekly discussion of one recent machine learning paper. " +
                "This week covers interpretability methods for language models. " +
                "Read the paper beforehand if you can.",
        ),
        CampusEvent(
            id = 107L,
            title = "Sunrise Yoga",
            category = "Wellness",
            location = "FitRec Studio 2",
            time = "Wed, Oct 28, 7:30 AM",
            description = "A gentle 45 minute flow to start the day. Mats are provided " +
                "and all levels are welcome.",
        ),
        CampusEvent(
            id = 108L,
            title = "Research Skills Workshop",
            category = "Academic",
            location = "Mugar Library, Room 302",
            time = "Thu, Oct 29, 3:00 PM",
            description = "Librarians show how to search academic databases, track citations " +
                "and manage references. Useful for anyone starting a thesis or term paper.",
        ),
        CampusEvent(
            id = 109L,
            title = "Halloween Movie Night",
            category = "Social",
            location = "GSU Auditorium",
            time = "Fri, Oct 30, 8:00 PM",
            description = "A double feature of classic horror films with free popcorn. " +
                "Costumes are encouraged and the best one wins a prize.",
        ),
        CampusEvent(
            id = 110L,
            title = "Build for Boston Hackathon",
            category = "Tech",
            location = "Photonics Center 206",
            time = "Sat, Oct 31, 9:00 AM",
            description = "A 12 hour hackathon focused on tools for the Boston community. " +
                "Teams of up to four. Meals, mentors and prizes are included.",
        ),
    )
}
