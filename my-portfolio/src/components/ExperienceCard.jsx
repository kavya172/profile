import React from "react";

const ExperienceCard = ({ title, company, bullets }) => {
  return (
    <div className="experience-card">
      <h2 className="exp-title">{title}</h2>
      <h3 className="exp-company">{company}</h3>

      <ul className="exp-list">
        {bullets.map((item, index) => (
          <li key={index}>{item}</li>
        ))}
      </ul>
    </div>
  );
};

export default ExperienceCard;
