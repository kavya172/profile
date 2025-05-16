import React from 'react'

const SideBar = () => {
  return (
    <div>
      <div className="sidebar"></div>
        <div className="sidebar__logo">
          <img src="/src/assets/kavya.jpeg" alt="Logo" />
        </div>
        <div className="sidebar__menu">
          <ul>
            <li>Home</li>
            <li>About</li>
            <li>Projects</li>
            <li>Contact</li>
          </ul>
      </div>
    </div>
  )
}

export default SideBar
