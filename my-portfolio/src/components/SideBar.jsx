import React from 'react'
import '../assets/styles/SideBar.css'
import porfileImage from '../assets/kavya.png'
const SideBar = () => {
  return (
    <>
    <div className="sidebar__container" style={{top:'0px'}}>
      <div className="sidebar__container--top">
        
      <div className="sidebar"></div>
        <div className="sidebar-logo">
          <img src={porfileImage} alt="Logo" />
        </div>
        <div className="sidebar__container--top--title"></div>
          <h1 className="sidebar__container--top--title--text">Srinivasa Kavya Yadav</h1>
          <p className="sidebar__container--top--title--subtitle">Full Stack Java Senior Developer | Spring Boot | React.js | AI/ML Enthusiast | Certified Yoga And Meditation Trainer | Passionate Trainer & Mentor at Galaxe Solutions, an Endava Company | Neuroscience Enthusiasts.</p>
      </div>
      </div>
    </>
  )
}

export default SideBar
