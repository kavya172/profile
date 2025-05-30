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
          <p className="sidebar__container--top--title--subtitle">
          Software Engineer | Full Stack Senior Developer | Tech Enthusiast | YouTuber | Trainer | Yoga Volunteer
          </p>


          <p>
          <a href="https://www.linkedin.com/in/kavyasrinivasa/" target="_blank" rel="noopener noreferrer">LinkedIn</a>
          </p>
      </div>
      </div>
    </>
  )
}

export default SideBar
