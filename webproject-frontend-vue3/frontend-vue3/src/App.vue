<template>
  <div
    v-if="isInitialized()"
    id="app"
    class="bg-theme-background min-h-screen text-theme-text"
  >
    <navbar v-if="!isAccessRestricted()" />
    <router-view />
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { mapActions, mapGetters } from "vuex";
import { useToast } from "vue-toastification";

const toast = useToast();

export default defineComponent({
  name: "App",
  created(): void {
    this.loadAppProperties().catch(() =>
      toast.error("Error while loading application properties")
    );
    this.loadCurrentUser().catch(() =>
      toast.error("Error while loading current user")
    );
    this.loadActiveTheme().catch(() =>
      toast.error("Error while loading active theme")
    );
  },
  methods: {
    ...mapActions(["loadAppProperties", "loadCurrentUser", "loadActiveTheme"]),
    ...mapGetters(["isInitialized", "isAccessRestricted"]),
  },
});
</script>

<style lang="scss">
@import "vue-select/src/scss/vue-select.scss";
@import "@/assets/styles/main.scss";
</style>
