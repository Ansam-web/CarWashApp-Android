package com.example.carwash.models;

public class EmployeeTeam {
    private int teamId;
    private String teamName;
    private String roleInTeam; // leader/member

    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public String getRoleInTeam() { return roleInTeam; }
    public void setRoleInTeam(String roleInTeam) { this.roleInTeam = roleInTeam; }
}
