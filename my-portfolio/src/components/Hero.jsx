// src/components/Hero.jsx
import React from 'react';
// NOTE: Make sure to import your image or use a public path
import Kavya from '../assets/kavya.png'; 

const Hero = () => (
  <div className="hero-container">
    <div className="hero-gradient-line"></div>
    <div className="hero-text-container">
      <h1 className="hero-greeting">
        Hi, I'm <span className="hero-name-highlight">Kavya</span>
      </h1>
      <p className="hero-bio">
        I'm a full-stack developer interested in front-end UI and back-end server apps
      </p>
    </div>
    
    <div className="hero-image-container">
      <img
        src={Kavya} 
        alt="Profile photo"
        className="hero-computer-image"
      />
    </div>

    <div className="scroll-indicator">
      {/* You might add an icon or a nested circle here */}
    </div>
  </div>
);

export default Hero;