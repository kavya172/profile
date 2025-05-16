import React from 'react'
import '../assets/styles/SideBar.css'
import porfileImage from '../assets/kavya.png'
const SideBar = () => {
  return (
    <>
    <div className="sidebar__container">
      <div className="sidebar__container--top">
        
      <div className="sidebar"></div>
        <div className="sidebar-logo">
          <img src={porfileImage} alt="Logo" />
        </div>
        
      </div>
      </div>
    </>
  )
}

export default SideBar
