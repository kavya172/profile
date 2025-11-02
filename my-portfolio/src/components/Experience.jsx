import React from 'react'
import ExperienceCard from './ExperienceCard'

const Experience = () => {
  return (
    <>
    <div>
    <h1 className="overview-title" style={{marginLeft:"75px"}}>Experience.</h1>
      <ExperienceCard
      title="Senior Developer"
      company="Galaxe Solutions India Pvt Ltd"
      bullets={[
        "As a Senior Full Stack Java Developer at Galaxe Solutions, Mainly Into Product Design and Development. I’ve been at the forefront of building scalable, Full-stack applications using Java (Spring Boot) and React.js. My role has extended beyond coding—I’ve been actively involved in designing microservices, implementing RESTful APIs, and integrating with databases like MongoDB, Postgresql and MySQL.",
        "What I enjoy most is being a bridge between the business and tech teams—understanding requirements deeply and delivering solutions that add real value. I’ve also led internal sessions on Java 17 features and modern frontend practices, helping uplift team capabilities."
      ]}
    />
    </div>
    <div>
     <ExperienceCard
      title="Developer"
      company="Galaxe Solutions India Pvt Ltd"
      bullets={[
        "As a committed software developer, I focus on delivering secure and efficient solutions that meet organizational and client needs. My role encompasses developing and maintaining code, identifying areas for improvement, and resolving defects to ensure system reliability. I collaborate closely with team members, contributing to the successful implementation of innovative solutions.",
        "Designed and developed RESTful APIs enabling seamless communication between frontend and backend systems. Created interactive and dynamic graph designs using React.js to visualize complex datasets effectively."
      ]}
    />
    </div>
    <div>
     <ExperienceCard
      title="Associate Developer"
      company="Galaxe Solutions India Pvt Ltd"
      bullets={[
        "As an Associate Developer, I have gained hands-on experience in designing, developing, and maintaining software solutions while adhering to organizational standards and best practices. My role involves collaborating with teams and clients to deliver secure, efficient, and innovative applications tailored to meet business needs."
      ]}
    />
    </div>
    </>
  )
}

export default Experience
