package _4.fortuna_bff.core

import _4.fortuna_bff.reports.Report
import jakarta.persistence.*
import java.util.*

@Entity
@Table(
    name = "students",
    indexes = [Index(name = "idx_students_email", columnList = "email", unique = true)]
)
class Student(
    @Id val id: String = UUID.randomUUID().toString(),
    val course: String,
    val advisor: String,
    val lattesProfile: String,

    @Column(name = "email", insertable=false, updatable=false) val email: String,

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val reports: List<Report> = mutableListOf(),

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email", referencedColumnName = "email")
    val user: User,
)
