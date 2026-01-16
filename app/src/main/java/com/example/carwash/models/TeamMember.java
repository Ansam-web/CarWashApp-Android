package com.example.carwash.models;

public class TeamMember {

    private String memberId;
    private String teamId;
    private String employeeId;

    // leader, member
    private String roleInTeam;

    // stored as String from MySQL (e.g. "2026-01-16 17:10:00")
    private String joinedAt;

    public TeamMember() {
        // empty constructor required
    }

    public TeamMember(String teamId, String employeeId, String roleInTeam) {
        this.teamId = teamId;
        this.employeeId = employeeId;
        this.roleInTeam = roleInTeam;
    }

    // ================= GETTERS & SETTERS =================

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getRoleInTeam() {
        return roleInTeam;
    }

    public void setRoleInTeam(String roleInTeam) {
        this.roleInTeam = roleInTeam;
    }

    public String getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(String joinedAt) {
        this.joinedAt = joinedAt;
    }

    @Override
    public String toString() {
        return "TeamMember{" +
                "teamId='" + teamId + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", role='" + roleInTeam + '\'' +
                '}';
    }
}
