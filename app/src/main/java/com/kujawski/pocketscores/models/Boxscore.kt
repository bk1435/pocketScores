package com.kujawski.pocketscores.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Boxscore(
    val teams: List<TeamStats>,
    val players: List<PlayerStats>
) : Parcelable

@Parcelize
data class TeamStats(
    val team: Team,
    val statistics: List<Statistic>,
    val players: List<PlayerStats>
) : Parcelable

@Parcelize
data class PlayerStats(
    val team: Team,
    val statistics: List<PlayerStatistic>,
    val homeAway: String,
) : Parcelable

@Parcelize
data class Statistic(
    val name: String,
    val displayValue: String
) : Parcelable

@Parcelize
data class PlayerStatistic(
    val name: String,
    val athletes: List<Athlete>
) : Parcelable

@Parcelize
data class Athlete(
    val athlete: AthleteDetail,
    val stats: List<String>
) : Parcelable

@Parcelize
data class AthleteDetail(
    val id: String,
    val displayName: String,
    val links: List<AthleteLink>
) : Parcelable

@Parcelize
data class AthleteLink(
    val rel: List<String>,
    val href: String
) : Parcelable
