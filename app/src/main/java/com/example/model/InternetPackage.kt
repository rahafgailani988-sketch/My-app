package com.example.model

data class InternetPackage(
    val code: String,
    val generalDataGb: Int,
    val socialDataGb: Int,
    val validityDays: Int,
    val title: String,
    val badge: String,
    val isPopular: Boolean = false,
    val networkSpeed: String = "5G Ultra Speed (Up to 1 Gbps)",
    val description: String = "Free High-Speed Global Data & Unlimited Social Media Tunnel"
) {
    val totalDataGb: Int get() = generalDataGb + socialDataGb
}

object PredefinedPackages {
    val packages = listOf(
        InternetPackage(
            code = "2211",
            generalDataGb = 25,
            socialDataGb = 25,
            validityDays = 15,
            title = "Global Starter Pass",
            badge = "15 Days",
            isPopular = false,
            networkSpeed = "5G Turbo Fast",
            description = "25GB General Web Browsing + 25GB Social Media (WhatsApp, IG, TikTok, YouTube)"
        ),
        InternetPackage(
            code = "2222",
            generalDataGb = 50,
            socialDataGb = 50,
            validityDays = 30,
            title = "Monthly Freedom Pack",
            badge = "30 Days",
            isPopular = true,
            networkSpeed = "5G Ultra Fast",
            description = "50GB General High-Speed Data + 50GB Social Media with 30 Days Full Access"
        ),
        InternetPackage(
            code = "2233",
            generalDataGb = 100,
            socialDataGb = 100,
            validityDays = 40,
            title = "Extended Mega Data",
            badge = "40 Days",
            isPopular = false,
            networkSpeed = "5G Ultra Fast Max",
            description = "100GB General High-Speed Data + 100GB Social Media Data for 40 Days"
        ),
        InternetPackage(
            code = "2244",
            generalDataGb = 150,
            socialDataGb = 150,
            validityDays = 60,
            title = "Global Traveler Duo",
            badge = "60 Days",
            isPopular = false,
            networkSpeed = "5G Hyper Speed",
            description = "150GB General High-Speed Data + 150GB Social Media Data for 60 Days"
        ),
        InternetPackage(
            code = "2255",
            generalDataGb = 200,
            socialDataGb = 200,
            validityDays = 90,
            title = "Ultimate Season Access",
            badge = "90 Days (Quarter)",
            isPopular = true,
            networkSpeed = "5G Infinity Speed",
            description = "200GB General High-Speed Data + 200GB Social Media Data for 90 Days"
        )
    )

    fun findByCode(code: String): InternetPackage? {
        val clean = code.trim()
        return packages.find { it.code == clean }
    }
}
