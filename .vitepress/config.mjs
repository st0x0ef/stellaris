import { defineConfig } from 'vitepress'

// https://vitepress.dev/reference/site-config
export default defineConfig({
  base: '/stellaris/',
  title: "The Stellaris Project Documentation",
  description: "Learn how to create your own planets !",
  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    nav: [
      { text: 'Home', link: '/' },
      { text: 'Create a planet', link: '/custom-planets' },
      { text: 'Configure the screen', link: '/custom-screen' }

    ],

    sidebar: [
      {
        text: 'Create a planet',
        items: [
          { text: 'The Planet file', link: '/custom-planets' },
          { text: 'The Screen files', link: '/custom-screen' }
        ]
      }
    ],

    socialLinks: [
      { icon: 'github', link: 'https://github.com/st0x0ef/stellaris' },
      { icon: 'youtube', link: 'https://www.youtube.com/channel/UCzrr9q1Afqu-Yb9i7nn1uPA' },
      { icon: 'discord', link: 'https://discord.gg/project-stellaris-698598471896268931' }

    ]
  },
  markdown: {
    math: true
  }
})
