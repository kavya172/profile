import ProjectCard from "./ProjectCard";

function Projects() {
  return (
    <div style = {{backgroundColor: "#E65E34"}}>
    <h1 className="overview-title" style={{marginLeft:"75px"}}>Projects.</h1>
    <div className="projects-container">
      <ProjectCard
        title="GxCapture - Tech Refresh Program"
        description="GxCapture-TechRefresh creates standardized processes and a platform that can be used to execute current and future lifecycle management program for any technologies."
        skills={["Java", "Spring Boot", "JavaScript", "React", "MS-SQL", "Nginx"]}
      />
      <ProjectCard
        title="GxCapture"
        description="GxCapture-TechRefresh creates standardized processes and a platform that can be used to execute current and future lifecycle management program for any technologies."
        skills={["Java", "Spring Boot", "JavaScript", "React", "MS-SQL", "Nginx"]}
      />
    </div>
      <div className="projects-container">
        <ProjectCard
        title="GxWorkflow"
        description="GxCapture-TechRefresh creates standardized processes and a platform that can be used to execute current and future lifecycle management program for any technologies."
        skills={["Java", "Spring Boot", "JavaScript", "React", "MS-SQL", "Nginx"]}
      />
      <ProjectCard
        title="GxClaims"
        description="GxCapture-TechRefresh creates standardized processes and a platform that can be used to execute current and future lifecycle management program for any technologies."
        skills={["Java", "Spring Boot", "JavaScript", "React", "MS-SQL", "Nginx"]}
      />
    </div>
    <div className="projects-container"></div>
    </div>
  );
}

export default Projects;
