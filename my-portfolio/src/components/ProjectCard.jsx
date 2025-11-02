import React from "react";

const ProjectCard = ({ title, description, skills }) => {
  return (
    <div className="project-card">
      <h3 className="project-title">{title}</h3>
      <hr className="divider" />

      <p className="project-description">{description}</p>

      <div className="project-skills">
        {skills.map((skill, index) => (
          <span className="skill-tag" key={index}>
            #{skill}
          </span>
        ))}
      </div>
    </div>
  );
};

export default ProjectCard;
